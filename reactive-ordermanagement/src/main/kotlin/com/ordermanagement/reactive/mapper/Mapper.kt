package com.ordermanagement.reactive.mapper

import com.ordermanagement.reactive.dto.*
import com.ordermanagement.reactive.model.*
import org.springframework.stereotype.Component

object Mapper {

    fun toUserEntity(dto: UserRequestDTO) : User =
        User(  name = dto.name, email = dto.email)

    fun toUserResponse(user : User) : UserResponseDTO =
        UserResponseDTO(user.id!!, user.name , user.email)

    fun toProductEntity(dto: ProductRequestDTO) : Product =
        Product(name = dto.name, price = dto.price)

    fun toProductResponse(product : Product) : ProductResponseDTO =
        ProductResponseDTO(product.id!!, product.name , product.price)

    fun toOrderResponse(order:Order):OrderResponseDTO =
        OrderResponseDTO(
            order.id!!,
            order.userId,
            order.items.map{
                OrderItemDTO(it.productId,it.quantity,it.price)
            },
            order.totalAmount,
            order.status
        )


}