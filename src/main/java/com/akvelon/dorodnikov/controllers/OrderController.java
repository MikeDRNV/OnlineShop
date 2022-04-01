package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.controllers.helpers.BaseController;
import com.akvelon.dorodnikov.domain.entites.OrderEntity;
import com.akvelon.dorodnikov.domain.services.OrderService;
import com.akvelon.dorodnikov.dto.OrderDTO;
import com.akvelon.dorodnikov.utils.MappingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for "Order" domain entity requests.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;

    /**
     * Returns ResponseEntity with HTTP status and list of {@link OrderDTO}-s.
     *
     * @param page Number of page.
     * @param size Limit of quantity of elements on one page.
     * @return ResponseEntity with HTTP status and list of orders.
     */
    @GetMapping
    public ResponseEntity getOrders(@RequestParam("page") int page, @RequestParam("size") int size) {
        List<OrderEntity> orderEntityList = orderService.getOrders(page, size);
        return new ResponseEntity(
                orderEntityList.stream().map(MappingUtils::mapToOrderDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Returns ResponseEntity with HTTP status and {@link OrderDTO}.
     *
     * @param id ID of OrderDTO to be received.
     * @return ResponseEntity with HTTP status and OrderDTO by ID.
     */
    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable int id) {
        OrderEntity orderEntity = orderService.get(id);
        if (orderEntity != null) {
            return new ResponseEntity(MappingUtils.mapToOrderDTO(orderEntity), HttpStatus.OK);
        } else {
            return messageWithStatus("Order with ID " + id + " does not exist", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Creates new {@link OrderDTO}.
     *
     * @param orderDTO OrderDTO to be added.
     * @return ResponseEntity with HTTP status and the order being saved in database.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody OrderDTO orderDTO) {
        OrderEntity orderEntity = MappingUtils.mapToOrderEntity(orderDTO);
        OrderEntity orderEntityCreated = orderService.create(orderEntity);
        return new ResponseEntity(MappingUtils.mapToOrderDTO(orderEntityCreated), HttpStatus.CREATED);
    }
}