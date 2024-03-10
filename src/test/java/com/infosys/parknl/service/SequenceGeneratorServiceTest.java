package com.infosys.parknl.service;

import com.infosys.parknl.model.DatabaseSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SequenceGeneratorServiceTest {

    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private SequenceGeneratorService sequenceGeneratorService;

    @Test
    public void generateSequence() {
        String seqName = "testSeq";
        long initialSequenceValue = 1;

        DatabaseSequence existingCounter = new DatabaseSequence(seqName, initialSequenceValue);
        when(mongoOperations.findAndModify(any(), any(Update.class), any(), eq(DatabaseSequence.class)))
                .thenReturn(existingCounter);

        long result = sequenceGeneratorService.generateSequence(seqName);

        assertEquals(initialSequenceValue, result);

        verify(mongoOperations, times(1))
                .findAndModify(any(), any(Update.class), any(), eq(DatabaseSequence.class));
    }
}