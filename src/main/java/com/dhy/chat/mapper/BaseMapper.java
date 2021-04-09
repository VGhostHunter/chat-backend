package com.dhy.chat.mapper;

import java.util.List;

/**
 * @author vghosthunter
 */
public interface BaseMapper<C, U, S, D, E> {

    /**
     * CreateDto 转 Entity
     * @param dto /
     * @return /
     */
    E cToEntity(C dto);

    /**
     * updateDto 转 Entity
     * @param dto
     * @return
     */
    E uToEntity(U dto);

    /**
     * Entity 转 SimpleDTO
     * @param entity /
     * @return /
     */
    S toSDto(E entity);

    /**
     * Entity 转 DetailDTO
     * @param entity /
     * @return /
     */
    D toDDto(E entity);

    /**
     * DTO集合转Entity集合
     * @param dtoList /
     * @return /
     */
    List<E> toEntity(List<C> dtoList);

    /**
     * Entity集合转DTO集合
     * @param entityList /
     * @return /
     */
    List <S> toDto(List<E> entityList);
}
