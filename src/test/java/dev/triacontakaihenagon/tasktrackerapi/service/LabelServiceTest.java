package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.LabelRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.Label;
import dev.triacontakaihenagon.tasktrackerapi.exception.LabelNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.LabelRepository;
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
class LabelServiceTest {

    @Mock LabelRepository labelRepository;
    @InjectMocks LabelService labelService;

    private Label label;

    @BeforeEach
    void setUp() {
        label = new Label();
        label.setId(1L);
        label.setName("urgent");
    }

    @Test
    void createLabel_savesWithGivenName() {
        LabelRequest request = new LabelRequest();
        request.setName("urgent");
        when(labelRepository.save(any(Label.class))).thenAnswer(inv -> inv.getArgument(0));

        Label result = labelService.createLabel(request);

        assertThat(result.getName()).isEqualTo("urgent");
    }

    @Test
    void updateLabel_updatesNameWhenFound() {
        LabelRequest request = new LabelRequest();
        request.setName("blocked");
        when(labelRepository.findById(1L)).thenReturn(Optional.of(label));
        when(labelRepository.save(label)).thenReturn(label);

        Label result = labelService.updateLabel(1L, request);

        assertThat(result.getName()).isEqualTo("blocked");
    }

    @Test
    void updateLabel_throwsWhenNotFound() {
        when(labelRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> labelService.updateLabel(404L, new LabelRequest()))
                .isInstanceOf(LabelNotFoundException.class);
    }

    @Test
    void deleteLabel_deletesWhenExists() {
        when(labelRepository.existsById(1L)).thenReturn(true);

        labelService.deleteLabel(1L);

        verify(labelRepository).deleteById(1L);
    }

    @Test
    void deleteLabel_throwsWhenNotFound() {
        when(labelRepository.existsById(404L)).thenReturn(false);

        assertThatThrownBy(() -> labelService.deleteLabel(404L))
                .isInstanceOf(LabelNotFoundException.class);

        verify(labelRepository, never()).deleteById(any());
    }
}