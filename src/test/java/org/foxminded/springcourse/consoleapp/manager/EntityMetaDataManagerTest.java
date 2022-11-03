package org.foxminded.springcourse.consoleapp.manager;

import org.foxminded.springcourse.consoleapp.dto.EntityMetaData;
import org.foxminded.springcourse.consoleapp.dto.EntityMetaDataCache;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.service.EntityMetaDataExtractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(PER_CLASS)
class EntityMetaDataManagerTest {

    @Spy
    private EntityMetaDataCache metaDataCache;

    @Mock
    private EntityMetaDataExtractor metaDataExtractor;

    @InjectMocks
    private EntityMetaDataManager metaDataManager;

    @BeforeAll
    void setUp() {
        openMocks(this);
        when(metaDataExtractor.getMetaData(Group.class)).thenReturn(new EntityMetaData("table", "id_column",
                List.of("id_column", "name_column"), List.of("name_column")));
    }

    @BeforeEach
    void clearCacheInvocationCount() {
        clearInvocations(metaDataExtractor);
        metaDataCache.clear();
    }

    @Test
    void getMetaData_shouldReturnExcepted_whenInputIsGroupClass() {
        Class<Group> groupClass = Group.class;

        EntityMetaData expected = new EntityMetaData("table", "id_column",
                List.of("id_column", "name_column"), List.of("name_column"));
        EntityMetaData actual = metaDataManager.getMetaData(groupClass);

        assertEquals(expected, actual);
    }

    @Test
    void getMetaData_shouldCallMetaDataExtractorOnceAndReturnExpectedFromCache_WhenInputIsRepeatable() {
        Class<Group> groupClass = Group.class;

        for (int i = 0; i < 5; i++) {
            EntityMetaData expected = new EntityMetaData("table", "id_column",
                    List.of("id_column", "name_column"), List.of("name_column"));
            EntityMetaData actual = metaDataManager.getMetaData(groupClass);
            assertEquals(expected, actual);
        }

        verify(metaDataExtractor, times(1)).getMetaData(groupClass);
        verify(metaDataCache, times(4)).get(groupClass);
    }
}
