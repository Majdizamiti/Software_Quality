package com.Shadows.SpringZ.service;

import com.Shadows.SpringZ.model.Category;
import com.Shadows.SpringZ.repository.CategoryRepository;
import com.Shadows.SpringZ.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setTitle("Electronics");
        testCategory.setDescription("Devices and gadgets");
    }

    @Test
    void createCategory_Success() {
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        Category created = categoryService.createCategory(testCategory);

        assertNotNull(created);
        assertEquals("Electronics", created.getTitle());
        verify(categoryRepository, times(1)).save(testCategory);
    }

    @Test
    void getAllCategories_Success() {
        when(categoryRepository.findAll()).thenReturn(List.of(testCategory));

        List<Category> list = categoryService.getAllCategories();

        assertEquals(1, list.size());
        assertEquals("Electronics", list.get(0).getTitle());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryByID_Found() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        Category result = categoryService.getCategoryByID(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getTitle());
    }

    @Test
    void getCategoryByID_NotFound_ThrowsException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.getCategoryByID(99L)
        );

        assertTrue(exception.getMessage().contains("Category not found"));
    }

    @Test
    void deleteCategory_Success() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
