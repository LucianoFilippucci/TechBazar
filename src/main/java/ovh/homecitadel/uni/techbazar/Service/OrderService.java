package ovh.homecitadel.uni.techbazar.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.Order.MongoDB.OrderDetailsEntity;
import ovh.homecitadel.uni.techbazar.Entity.Order.MongoDB.StoreOrderEntity;
import ovh.homecitadel.uni.techbazar.Entity.Order.OrderEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Entity.User.MongoDB.CartEntity;
import ovh.homecitadel.uni.techbazar.Entity.User.UserAddressEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ProductQuantityUnavailableException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Helpers;
import ovh.homecitadel.uni.techbazar.Helper.Model.*;
import ovh.homecitadel.uni.techbazar.Helper.Model.Cart.ProductInCart;
import ovh.homecitadel.uni.techbazar.Helper.OrderStatusEnum;
import ovh.homecitadel.uni.techbazar.Helper.UnifiedServiceAccess;
import ovh.homecitadel.uni.techbazar.Repository.Order.MongoDB.OrderDetailsRepository;
import ovh.homecitadel.uni.techbazar.Repository.Order.MongoDB.StoreOrderRepository;
import ovh.homecitadel.uni.techbazar.Repository.Order.OrderRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductModelRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.MongoDB.CartRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.UserAddressRepository;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CartRepository cartRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProductRepository productRepository;
    private final ProductModelRepository productModelRepository;
    private final StoreOrderRepository storeOrderRepository;
    private final UnifiedServiceAccess unifiedServiceAccess;

    public OrderService(UnifiedServiceAccess unifiedServiceAccess, OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, CartRepository cartRepository, UserAddressRepository userAddressRepository, ProductRepository productRepository, ProductModelRepository productModelRepository, StoreOrderRepository storeOrderRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.cartRepository = cartRepository;
        this.userAddressRepository = userAddressRepository;
        this.productRepository = productRepository;
        this.productModelRepository = productModelRepository;
        this.storeOrderRepository = storeOrderRepository;
        this.unifiedServiceAccess = unifiedServiceAccess;
    }

    @Transactional
    public String placeOrder(String cartId, String userId, Long userAddressId) throws ObjectNotFoundException, ProductQuantityUnavailableException {
        String orderId  = Helpers.GenerateUID();
        Optional<CartEntity> tmp = this.cartRepository.findByCartId(cartId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Cart not Found");
        CartEntity cart = tmp.get();
        
        Optional<UserAddressEntity> tmp2 = this.userAddressRepository.findById(userAddressId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Address Not Found");
        UserAddressEntity address = tmp2.get();
        
        HashMap<String, List<ProductInCart>> storesProduct = new HashMap<>();


        BigDecimal orderTotal = BigDecimal.ZERO;
        
        for(ProductInCart pic : cart.getProducts()) {
            Optional<ProductEntity> tmp4 = this.productRepository.findByProductId(pic.getProductId());
            if(tmp4.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
            ProductEntity product = tmp4.get();

            Optional<ProductModelEntity> tmp6 = this.productModelRepository.findById(pic.getProductModelId());
            if(tmp6.isEmpty()) throw new ObjectNotFoundException("Product Model Not Found");
            ProductModelEntity model = tmp6.get();

            if((model.getConfigQty() - pic.getQty()) < 0) throw new ProductQuantityUnavailableException("The Selected Quantity is unavailable. MAX: " + model.getConfigQty());
            model.setConfigQty(model.getConfigQty() - pic.getQty());
            model.setConfigSoldQty(model.getConfigSoldQty() + pic.getQty());

            product.setProductTotalSold(product.getProductTotalSold() + pic.getQty());
            product.setProductQuantity(product.getProductQuantity() - pic.getQty());

            if(!storesProduct.containsKey(product.getStoreId())) {
                List<ProductInCart> tmp5 = new ArrayList<>();
                tmp5.add(pic);
                storesProduct.put(product.getStoreId(), tmp5);
            } else {
                if(!storesProduct.get(product.getStoreId()).contains(pic))
                    storesProduct.get(product.getStoreId()).add(pic);
            }

            orderTotal = orderTotal.add(pic.getProductPrice().multiply(BigDecimal.valueOf(pic.getQty())));

            this.productModelRepository.save(model);
        }

        for(String store : storesProduct.keySet()) {
            BigDecimal storeTotal = BigDecimal.ZERO;
            for(ProductInCart pic : storesProduct.get(store)) {
                storeTotal = storeTotal.add(pic.getProductPrice().multiply(BigDecimal.valueOf(pic.getQty())));
            }

            StoreOrderEntity storeOrder = new StoreOrderEntity();
            storeOrder.setOrderDate(LocalDateTime.now());
            storeOrder.setOrderId(orderId);
            storeOrder.setTotal(storeTotal);
            storeOrder.setUserAddress(address.toString());
            storeOrder.setStoreId(store);
            storeOrder.setProducts(storesProduct.get(store));

            this.storeOrderRepository.save(storeOrder);

        }
        OrderEntity order = new OrderEntity(
                orderId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                OrderStatusEnum.PENDING,
                userId,
                address.toString(),
                orderTotal,
                "",
                "",
                "",
                ""
        );

        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        orderDetails.setOrderId(orderId);
        orderDetails.setProducts(cart.getProducts());
        orderDetails.setTotal(orderTotal);


        this.orderRepository.saveAndFlush(order);
        this.orderDetailsRepository.save(orderDetails);
        cart.getProducts().clear();
        cart.setUpdatedAt(LocalDateTime.now());
        this.cartRepository.save(cart);
        return orderId;
    }

    @Transactional
    public List<OrderListModel> getOrders(String userId) throws ObjectNotFoundException {
        Collection<OrderEntity> orders = this.orderRepository.findOrderEntitiesByUserId(userId);
        List<OrderListModel> olm = new ArrayList<>();

        for(OrderEntity order : orders) {
            olm.add(mapOrderDetails(order));
        }
        return olm;
    }

    @Transactional
    public OrderListModel getOrder(String userId, String orderId) throws ObjectNotFoundException, UnauthorizedAccessException {
        Optional<OrderEntity> tmp = this.orderRepository.findById(orderId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Order {" + orderId + "} Not Found");
        OrderEntity order = tmp.get();

        if(!order.getUserId().equals(userId)) throw new UnauthorizedAccessException("This order's not yours buddy");

        Optional<OrderDetailsEntity> tmp2 = this.orderDetailsRepository.findByOrderId(orderId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Order {" + orderId + "} Details not found.");
        OrderDetailsEntity orderDetails = tmp2.get();

        OrderListModel olm = new OrderListModel();
        olm.setOrderTotal(order.getOrderTotal());
        olm.setContactInfo(order.getContactInfo());
        olm.setOrderStatus(order.getOrderStatus());
        olm.setOrderId(orderId);
        olm.setOrderDate(order.getOrderDate());
        olm.setLastUpdate(order.getOrderUpdatedAt());
        olm.setExpress(order.getExpress());
        olm.setShippingAddr(order.getShippingAddress());
        olm.setTrackingCode(order.getTrackingCode());
        olm.setNote(order.getNote());
        olm.setUserId(userId);

        ArrayList<ProductInCartResponse> products = new ArrayList<>();
        for(ProductInCart pic: orderDetails.getProducts()) {
            ProductResponse product = this.unifiedServiceAccess.getProductEntity(pic.getProductId());
            Model models = new Model();
            // Technically every PiC should have only 1 model, because every time we hadd
            // more product of different models, we create a new entry in PiC
            // and if, eventually, there are more products with same model we just increase the counter duh
            for(Model model : product.getModels()) {
                if(model.getModelId().equals(pic.getProductModelId()))
                    models = model;
            }

            products.add(new ProductInCartResponse(
                    product.getProductId(),
                    models.getModelId(),
                    models.getConfiguration(),
                    product.getProductName(),
                    models.getConfigColor(),
                    pic.getQty(),
                    pic.getProductPrice(),
                    pic.getIva(),
                    pic.getStoreId()));
        }
        olm.setProducts(products);

        return olm;
    }

    @Transactional
    public List<OrderListModel> getStoreOrders(String storeId) throws ObjectNotFoundException {
        Collection<StoreOrderEntity> storeOrderEntities = this.storeOrderRepository.findAllByStoreId(storeId);
        List<OrderListModel> olm = new ArrayList<>();

        for(StoreOrderEntity order : storeOrderEntities) {
            Optional<OrderEntity> tmp = this.orderRepository.findById(order.getOrderId());
            if(tmp.isEmpty()) throw new ObjectNotFoundException("Order Not Found");
            OrderEntity oe = tmp.get();
            OrderListModel om = new OrderListModel();
            om.setOrderTotal(order.getTotal());
            om.setOrderId(order.getOrderId());
            om.setOrderDate(order.getOrderDate());
            om.setOrderStatus(oe.getOrderStatus());
            om.setUserId(oe.getUserId());
            om.setNote(oe.getNote());
            om.setExpress(oe.getExpress());
            om.setShippingAddr(order.getUserAddress());
            om.setTrackingCode(oe.getTrackingCode());
            om.setOrderDate(order.getOrderDate());
            om.setLastUpdate(oe.getOrderUpdatedAt());

            ArrayList<ProductInCartResponse> products = new ArrayList<>();
            for(ProductInCart pic: order.getProducts()) {
                ProductResponse product = this.unifiedServiceAccess.getProductEntity(pic.getProductId());
                Model models = new Model();
                // Technically every PiC should have only 1 model, because every time we hadd
                // more product of different models, we create a new entry in PiC
                // and if, eventually, there are more products with same model we just increase the counter duh
                for(Model model : product.getModels()) {
                    if(model.getModelId().equals(pic.getProductModelId()))
                        models = model;
                }

                products.add(new ProductInCartResponse(
                        product.getProductId(),
                        models.getModelId(),
                        models.getConfiguration(),
                        product.getProductName(),
                        models.getConfigColor(),
                        pic.getQty(),
                        pic.getProductPrice(),
                        pic.getIva(),
                        pic.getStoreId()));
            }
            om.setProducts(products);
            olm.add(om);
        }
        return olm;
    }


    @Transactional
    public OrderListModel getStoreOrder(String storeId, String orderId) throws ObjectNotFoundException, UnauthorizedAccessException {
        Optional<StoreOrderEntity> tmp = this.storeOrderRepository.findByOrderId(orderId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Order Not Found");
        StoreOrderEntity storeOrder = tmp.get();

        if(!storeOrder.getStoreId().equals(storeId)) throw new UnauthorizedAccessException("This Order not yours buddy");

        Optional<OrderEntity> tmp2 = this.orderRepository.findById(orderId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Order Not Found");
        OrderEntity orderEntity = tmp2.get();

        OrderListModel olm = new OrderListModel();
        olm.setOrderTotal(storeOrder.getTotal());
        olm.setOrderId(orderId);
        olm.setUserId(orderEntity.getUserId());
        olm.setOrderStatus(orderEntity.getOrderStatus());
        olm.setNote(orderEntity.getNote());
        olm.setOrderDate(storeOrder.getOrderDate());
        olm.setLastUpdate(orderEntity.getOrderUpdatedAt());
        olm.setExpress(orderEntity.getExpress());
        olm.setTrackingCode(orderEntity.getTrackingCode());
        olm.setShippingAddr(storeOrder.getUserAddress());
        olm.setContactInfo(orderEntity.getContactInfo());

        ArrayList<ProductInCartResponse> products = new ArrayList<>();
        for(ProductInCart pic: storeOrder.getProducts()) {
            ProductResponse product = this.unifiedServiceAccess.getProductEntity(pic.getProductId());
            Model models = new Model();
            // Technically every PiC should have only 1 model, because every time we hadd
            // more product of different models, we create a new entry in PiC
            // and if, eventually, there are more products with same model we just increase the counter duh
            for(Model model : product.getModels()) {
                if(model.getModelId().equals(pic.getProductModelId()))
                    models = model;
            }

            products.add(new ProductInCartResponse(
                    product.getProductId(),
                    models.getModelId(),
                    models.getConfiguration(),
                    product.getProductName(),
                    models.getConfigColor(),
                    pic.getQty(),
                    pic.getProductPrice(),
                    pic.getIva(),
                    pic.getStoreId()));
        }

        olm.setProducts(products);

        return olm;
    }

    @Transactional
    public boolean updateOrderInfo(String storeId, String orderId, UpdateOrderModel orderUpdate) throws ObjectNotFoundException, UnauthorizedAccessException{
        Optional<StoreOrderEntity> tmp = this.storeOrderRepository.findByOrderId(orderId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Order Not Found");
        StoreOrderEntity storeOrder = tmp.get();

        if(!storeOrder.getStoreId().equals(storeId)) throw new UnauthorizedAccessException("This order not yours buddy");

        Optional<OrderEntity> tmp2 = this.orderRepository.findById(orderId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Order Not Found");
        OrderEntity order = tmp2.get();

        order.setTrackingCode(orderUpdate.getTrackingCode());
        order.setExpress(orderUpdate.getExpress());
        order.setOrderStatus(orderUpdate.getOrderStatus());
        this.orderRepository.save(order);
        return true;
    }

    private OrderListModel mapOrderDetails(OrderEntity order) throws ObjectNotFoundException {
        Optional<OrderDetailsEntity> tmp = this.orderDetailsRepository.findByOrderId(order.getOrderId());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Order Detail Not Found");
        OrderDetailsEntity orderDetails = tmp.get();

        OrderListModel om = new OrderListModel();
        om.setOrderId(order.getOrderId());
        om.setOrderDate(order.getOrderDate());
        om.setOrderStatus(order.getOrderStatus());
        om.setNote(order.getNote());
        om.setLastUpdate(order.getOrderUpdatedAt());
        om.setContactInfo(order.getContactInfo());
        om.setTrackingCode(order.getTrackingCode());
        om.setShippingAddr(order.getShippingAddress());
        om.setExpress(order.getExpress());
        om.setUserId(order.getUserId());


        ArrayList<ProductInCartResponse> products = new ArrayList<>();
        for(ProductInCart pic: orderDetails.getProducts()) {
            ProductResponse product = this.unifiedServiceAccess.getProductEntity(pic.getProductId());
            Model models = new Model();
            // Technically every PiC should have only 1 model, because every time we hadd
            // more product of different models, we create a new entry in PiC
            // and if, eventually, there are more products with same model we just increase the counter duh
            // but since in the frontend I fucked up and now is a bit tricky, probably i should just return a list even thou
            // we know it's just 1 elem
            for(Model model : product.getModels()) {
                if(model.getModelId().equals(pic.getProductModelId()))
                    models = model;
            }

            products.add(new ProductInCartResponse(
                    product.getProductId(),
                    models.getModelId(),
                    models.getConfiguration(),
                    product.getProductName(),
                    models.getConfigColor(),
                    pic.getQty(),
                    pic.getProductPrice(),
                    pic.getIva(),
                    pic.getStoreId()));
        }

        om.setProducts(products);


        om.setOrderTotal(order.getOrderTotal());
        return om;
    }

}
