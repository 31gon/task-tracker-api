package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.CategoryRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.Category;
import dev.triacontakaihenagon.tasktrackerapi.exception.CategoryNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock CategoryRepository categoryRepository;
    @InjectMocks CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Work");
    }

    @Test
    void createCategory_savesWithGivenName() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Work");
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        Category result = categoryService.createCategory(request);

        assertThat(result.getName()).isEqualTo("Work");
    }

    @Test
    void updateCategory_updatesNameWhenFound() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Personal");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.updateCategory(1L, request);

        assertThat(result.getName()).isEqualTo("Personal");
    }

    @Test
    void updateCategory_throwsWhenNotFound() {
        when(categoryRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(404L, new CategoryRequest()))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void deleteCategory_deletesWhenExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_throwsWhenNotFound() {
        when(categoryRepository.existsById(404L)).thenReturn(false);

        assertThatThrownBy(() -> categoryService.deleteCategory(404L))
                .isInstanceOf(CategoryNotFoundException.class);

        verify(categoryRepository, never()).deleteById(any());
    }
}