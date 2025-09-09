package com.readstack.library.book;

import com.readstack.library.book.dto.BookCreateUpdateDto;
import com.readstack.library.book.dto.BookDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDTO(Book book);

    @Mapping(target = "id", ignore = true)
    Book toBook(BookCreateUpdateDto bookDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateBookFromDto(BookCreateUpdateDto bookDto, @MappingTarget Book book);
}
