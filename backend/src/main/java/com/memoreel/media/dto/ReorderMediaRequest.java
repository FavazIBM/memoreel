package com.memoreel.media.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for reordering media items
 * Contains the new order index for a media item
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReorderMediaRequest {
    
    @NotNull(message = "New order index is required")
    @Min(value = 0, message = "Order index must be non-negative")
    private Integer newOrderIndex;
}

// Made with Bob
