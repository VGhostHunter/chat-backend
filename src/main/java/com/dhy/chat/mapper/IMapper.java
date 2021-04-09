package com.dhy.chat.mapper;

import java.util.List;

/**
 * @author vghosthunter
 */
public interface IMapper<C, D, E> {

    /**
     * DTO转Entity
     * @param dto /
     * @return /
     */
    E toEntity(C dto);

    /**
     * Entity转DTO
     * @param entity /
     * @return /
     */
    D toDto(E entity);

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
    List <D> toDto(List<E> entityList);
}
