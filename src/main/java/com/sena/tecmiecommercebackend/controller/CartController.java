package com.sena.tecmiecommercebackend.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sena.tecmiecommercebackend.common.ApiResponse;
import com.sena.tecmiecommercebackend.dto.cart.AddToCardDto;
import com.sena.tecmiecommercebackend.dto.cart.CartDto;
import com.sena.tecmiecommercebackend.exceptions.AuthenticationFailException;
import com.sena.tecmiecommercebackend.exceptions.CartItemNotExistException;
import com.sena.tecmiecommercebackend.exceptions.ProductNotExistException;
import com.sena.tecmiecommercebackend.repository.entity.Product;
import com.sena.tecmiecommercebackend.repository.entity.User;
import com.sena.tecmiecommercebackend.service.AuthenticationService;
import com.sena.tecmiecommercebackend.service.CartService;
import com.sena.tecmiecommercebackend.service.ProductService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    // @GetMapping("/")
    // public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token) throws AuthenticationFailException {
    //     authenticationService.authenticate(token);
    //     User user = authenticationService.getUser(token);
    //     CartDto cartDto = cartService.listCartItems(user);
    //     return new ResponseEntity<>(cartDto, HttpStatus.OK);
    // }

    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token) throws AuthenticationFailException {
        System.out.println("Authenticating token: " + token); // Log token before authentication
        authenticationService.authenticate(token);

        User user = authenticationService.getUser(token);
        if (user == null) {
            System.out.println("Authentication failed: User not found for token: " + token); // Log failure
            throw new AuthenticationFailException("Authentication token not valid.");
        }
        
        System.out.println("Authenticated user: " + user.getEmail()); // Log successful authentication
        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCardDto addToCartDto,
                                                 @RequestParam("token") String token) throws AuthenticationFailException, ProductNotExistException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);

        Product product = productService.getProductById(addToCartDto.getProductId());
        System.out.println("Product to add "+  product.getName());
        cartService.addToCart(addToCartDto, product, user);
        return new ResponseEntity<>(new ApiResponse(true, "Added to cart."), HttpStatus.CREATED);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(@Valid @RequestBody AddToCardDto cartDto,
                                                      @RequestParam("token") String token) throws AuthenticationFailException,ProductNotExistException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        cartService.updateCartItem(cartDto, user);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been updated."), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") Integer cartItemId,@RequestParam("token") String token) throws AuthenticationFailException, CartItemNotExistException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        cartService.deleteCartItem(cartItemId, user);
        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed."), HttpStatus.OK);
    }
}
