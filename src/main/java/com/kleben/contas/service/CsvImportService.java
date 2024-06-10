package com.kleben.contas.service;

import com.kleben.contas.controller.dto.ContaDTO;
import com.kleben.contas.model.Conta;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class CsvImportService {

    public List<Conta> importarCsv(MultipartFile file) throws Exception {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<Conta> csvToBean = new CsvToBeanBuilder<Conta>(reader)
                    .withType(Conta.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }

}