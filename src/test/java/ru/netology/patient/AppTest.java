package ru.netology.patient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AppTest {

    private static PatientInfoFileRepository patientInfoFileRepository;
    private static SendAlertService sendAlertService;
    private static MedicalServiceImpl medicalService;

    @BeforeAll
    static void create_medical_service() {
        patientInfoFileRepository =
                Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(Mockito.any()))
                .thenReturn(new PatientInfo("Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));
    }

    @BeforeEach
    void new_medical_service() {
        sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
    }

    @Test
    void checkBloodPressure_test() {
        medicalService.checkBloodPressure(Mockito.any(), new BloodPressure(150, 100));

        Mockito.verify(sendAlertService).send(Mockito.any());
    }

    @Test
    void checkTemperature_test() {
        medicalService.checkTemperature(Mockito.any(), new BigDecimal("39"));

        Mockito.verify(sendAlertService).send(Mockito.any());
    }

    @Test
    void all_indicators_is_ok_test() {
        medicalService.checkTemperature(Mockito.any(), new BigDecimal("36"));
        medicalService.checkBloodPressure(Mockito.any(), new BloodPressure(120, 80));

        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.any());
    }

}
