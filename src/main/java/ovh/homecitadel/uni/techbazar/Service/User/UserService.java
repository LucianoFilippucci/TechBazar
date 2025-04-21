package ovh.homecitadel.uni.techbazar.Service.User;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.User.MongoDB.CartEntity;
import ovh.homecitadel.uni.techbazar.Entity.User.UserAddressEntity;
import ovh.homecitadel.uni.techbazar.Entity.UserLiked;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.NotNullException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Helpers;
import ovh.homecitadel.uni.techbazar.Helper.Model.User.User;
import ovh.homecitadel.uni.techbazar.Helper.Model.User.UserAddress;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MessageModel;
import ovh.homecitadel.uni.techbazar.Repository.MongoDB.UserLikedRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.MongoDB.CartRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.UserAddressRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.UserCartRepository;
import ovh.homecitadel.uni.techbazar.Security.KeycloakSecurityUtil;
import ovh.homecitadel.uni.techbazar.Service.NotificationSystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final KeycloakSecurityUtil keycloakSecurityUtil;
    private final UserCartRepository userCartRepository;
    private final UserAddressRepository userAddressRepository;
    private final CartRepository cartRepository;

    private final NotificationSystem notificationSystem;
    private final UserLikedRepository userLikedRepository;


    @Value("${realm}")
    private String realm;

    public UserService(UserLikedRepository userLikedRepository, KeycloakSecurityUtil keycloakSecurityUtil, UserCartRepository userCartRepository, UserAddressRepository userAddressRepository, CartRepository cartRepository, NotificationSystem notificationSystem) {
        this.keycloakSecurityUtil = keycloakSecurityUtil;
        this.userCartRepository = userCartRepository;
        this.userAddressRepository = userAddressRepository;
        this.cartRepository = cartRepository;
        this.notificationSystem = notificationSystem;
        this.userLikedRepository = userLikedRepository;
    }


    @Transactional
    public boolean createUser(User user) {
        String cartId = Helpers.GenerateUID();

        UserRepresentation userRep = mapUserRep(user, cartId);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();


        Response response = keycloak.realm(realm).users().create(userRep);
        if(response.getStatus() == 201) {
            List<UserRepresentation> userR = keycloak.realm(realm).users().searchByEmail(user.getEmail(), true);

            String userId = userR.get(0).getId();

            List<String> roles = new ArrayList<>();
            roles.add("user");
            if(user.getPIva() != null) roles.add("store");

            setUserRole(userId, roles);
            //UserCartEntity cartEntity = new UserCartEntity(cartId, userId);
            CartEntity cart = new CartEntity();
            cart.setCartId(cartId);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUpdatedAt(LocalDateTime.now());
            cart.setProducts(new ArrayList<>());
            CartEntity ct = this.cartRepository.save(cart);

            UserLiked userLiked = new UserLiked();
            userLiked.setLikedReviews(new ArrayList<>());
            userLiked.setUserId(userId);
            userLiked.setStoreLiked(new ArrayList<>());

            this.userLikedRepository.save(userLiked);
            if(ct.getCreatedAt() != null)
                return true;
            else {
                // TODO: IDK PROBLEMS.
                return false;
            }

        }
        return false;
    }

    @Transactional
    public void setUserRole(String userId, List<String> roles) {
        List<RoleRepresentation> roleList = rolesToRealmRoleRepresentation(roles);
        keycloakSecurityUtil.getKeycloakInstance().realm(realm).users().get(userId).roles().realmLevel().add(roleList);
    }

    @Transactional
    public List<UserAddressEntity> getUserAddress(String userId) throws ObjectNotFoundException{
        Keycloak keycloak = this.keycloakSecurityUtil.getKeycloakInstance();

       List<UserAddressEntity> tmp = this.userAddressRepository.findByUserId(userId);
       if(tmp.isEmpty()) throw new ObjectNotFoundException("User has no Addresses.");

       return tmp;

    }

    @Transactional
    public UserAddressEntity getAddressById(String userId, Long addressId) throws UnauthorizedAccessException, ObjectNotFoundException {
        Optional<UserAddressEntity> tmp = this.userAddressRepository.findById(addressId);

        if(tmp.isEmpty()) throw new ObjectNotFoundException("No Address Found with given id.");

        UserAddressEntity address = tmp.get();

        if(userId.equals(address.getUserId()))
            return address;
        throw new UnauthorizedAccessException("This Address is not yours.");
    }


    @Transactional
    public UserAddressEntity newUserAddress(String userId, UserAddress userAddress) throws NotNullException {
        System.out.println("INSIDE THE NEW ADDRESS SERVICE");

        if(userAddress.getStreet() == null) throw new NotNullException("Street can't be null");
        if(userAddress.getState() == null) throw new NotNullException("State can't be null");
        if(userAddress.getCountry() == null) throw new NotNullException("Country can't be null");
        if(userAddress.getPostalCode() == null) throw new NotNullException("Postal Code can't be null");
        if(userAddress.getAddressName() == null) throw new NotNullException("Address NAme can't be null");

        UserAddressEntity address = new UserAddressEntity(userAddress, userId);

        System.out.println(userAddress);
        System.out.println(address);



        return this.userAddressRepository.save(address);
    }

    @Transactional
    public UserAddressEntity setDefaultAddress(Long addressId) throws ObjectNotFoundException {
        Optional<UserAddressEntity> tmp = this.userAddressRepository.findById(addressId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Address Not Found.");
        UserAddressEntity address = tmp.get();

        Optional<UserAddressEntity> oldDefault = this.userAddressRepository.findByIsDefault(true);
        oldDefault.ifPresent(old -> {
            old.setDefault(false);
            this.userAddressRepository.save(old);
        });

        address.setDefault(true);
        return this.userAddressRepository.save(address);
    }

    @Transactional
    public UserAddressEntity editUserAddress(String userId, UserAddress userAddress, Long addressId) throws ObjectNotFoundException, UnauthorizedAccessException{
        Optional<UserAddressEntity> tmp = this.userAddressRepository.findById(addressId);

        if(tmp.isEmpty()) throw new ObjectNotFoundException("Address not found.");
        UserAddressEntity entity = tmp.get();
        if(userId.equals(entity.getUserId())) {
            if(!entity.getCivic().equals(userAddress.getCivic()))
                entity.setCivic(userAddress.getCivic());
            if(!entity.getState().equals(userAddress.getState()))
                entity.setState(userAddress.getState());
            if(!entity.getStreet().equals(userAddress.getStreet()))
                entity.setStreet(userAddress.getStreet());
            if(!entity.getCountry().equals(userAddress.getCountry()))
                entity.setCountry(userAddress.getCountry());
            if(!entity.getPostalCode().equals(userAddress.getPostalCode()))
                entity.setPostalCode(userAddress.getPostalCode());

            return this.userAddressRepository.save(entity);
        } else throw new UnauthorizedAccessException("This address is not yours.");
    }

    @Transactional
    public void deleteAddress(String userId, Long addressId) throws UnauthorizedAccessException, ObjectNotFoundException {
        Optional<UserAddressEntity> tmp = this.userAddressRepository.findById(addressId);

        if(tmp.isEmpty()) throw new ObjectNotFoundException("Address Not Found Buddy.");
        UserAddressEntity address = tmp.get();

        if(!address.getUserId().equals(userId)) throw new UnauthorizedAccessException("This address is not yours buddy.");

        this.userAddressRepository.delete(address);
    }

    @Transactional
    public int getUnreadNotification(String userId) throws ObjectNotFoundException {
        return this.notificationSystem.getUnreadNotifications(userId);
    }

    @Transactional
    public void sendMessage(MessageModel message) throws ObjectNotFoundException {
        this.notificationSystem.createNotification(message);
    }

    // ------ PRIVATE USER HELPERS ------ //

    private List<RoleRepresentation> rolesToRealmRoleRepresentation(List<String> roles) {
        List<RoleRepresentation> existingRoles = keycloakSecurityUtil.getKeycloakInstance().realm(realm).roles().list();

        List<String> serverRoles = existingRoles.stream().map(RoleRepresentation::getName).toList();
        List<RoleRepresentation> resultRoles = new ArrayList<>();

        for(String role : roles) {
            int index = serverRoles.indexOf(role);
            if(index != -1) {
                resultRoles.add(existingRoles.get(index));
            } else {
                System.out.println("Role doesn't exist");
            }
        }
        return resultRoles;
    }


    private User mapUser(UserRepresentation userRepresentation) {
        User user = new User();

        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setEmail(userRepresentation.getEmail());
        user.setUsername(userRepresentation.getUsername());
        return user;
    }

    private UserRepresentation mapUserRep(User user, String cartId){
        UserRepresentation userRep = new UserRepresentation();
        if(user.getPIva() != null) {
            userRep.singleAttribute("partitaIva", user.getPIva());

        }

        userRep.singleAttribute("cartId", cartId);
        userRep.singleAttribute("phoneNumber", user.getPhoneNumber());
        userRep.setUsername(user.getUsername());
        userRep.setLastName(user.getLastName());
        userRep.setFirstName(user.getFirstName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true); // TODO: change to false, then use email verification
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep;
    }
}
