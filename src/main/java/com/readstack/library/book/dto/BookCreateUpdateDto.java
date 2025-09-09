package com.readstack.library.book.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateUpdateDto {
    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 20)
    private String isbn;

    @Min(1450)
    @Max(2100)
    private Integer publishedYear;
}
