package com.kleben.contas.controller;

import com.kleben.contas.controller.dto.ContaDTO;
import com.kleben.contas.controller.dto.PeriodoDTO;
import com.kleben.contas.enums.SituacaoEnum;
import com.kleben.contas.model.Conta;
import com.kleben.contas.repository.ContaRepository;
import com.kleben.contas.service.CsvImportService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private CsvImportService csvImportService;
    private static final Logger logger = LoggerFactory.getLogger(ContaController.class);


    @GetMapping
    public ResponseEntity<Page<Conta>> listar(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Max(100) Integer size) {

        try {
//            throw new Exception("teste");
            logger.info("Buscando contas");
            return ResponseEntity.ok(contaRepository.findAll(PageRequest.of(page, size)));
        } catch (Exception e) {
            logger.error("Erro ao consultar contas: "+e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Optional<Conta>> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contaRepository.findById(id));
        } catch (Exception e) {
            logger.error("Erro ao consultar conta por ID: "+e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/buscar/a-pagar/data-vencimento")
    public ResponseEntity<Page<Conta>> buscarPorDataVencimento(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Max(100) Integer size,
            @RequestBody ContaDTO contaDTO) {

        try {
            if (contaDTO.getDataVencimento() != null)
                return ResponseEntity.ok(contaRepository.findByDataVencimento(contaDTO.getDataVencimento(), PageRequest.of(page, size)));

            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            logger.error("Erro ao consultar conta por daat de vencimento: "+e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/buscar/a-pagar/descricao")
    public ResponseEntity<Page<Conta>> buscarPorDescricao(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Max(100) Integer size,
            @RequestBody ContaDTO contaDTO) {

        try {
            if (contaDTO.getDescricao() != null && !contaDTO.getDescricao().isEmpty())
                return ResponseEntity.ok(contaRepository.findByDescricaoContainingIgnoreCase(contaDTO.getDescricao(), PageRequest.of(page, size)));

            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            logger.error("Erro ao consultar conta por daat de vencimento: "+e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/buscar/a-pagar/descricao-e-data-vencimento")
    public ResponseEntity<Page<Conta>> buscarPorDescricaoEdataVencimento(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Max(100) Integer size,
            @RequestBody ContaDTO contaDTO) {
        try {
            return ResponseEntity.ok(contaRepository.findByDescricaoAndDataVencimento(contaDTO.getDescricao(), contaDTO.getDataVencimento(), PageRequest.of(page, size)));
        } catch (Exception e) {
            logger.error("Erro ao consultar contas por descricao e data vencimento: "+e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/total-pago-por-periodo")
    public ResponseEntity<BigDecimal> totalPorPeriodo(@RequestBody PeriodoDTO periodoDTO) {
        return ResponseEntity.ok(contaRepository.totalPorPeriodo(periodoDTO.getDataInicial(), periodoDTO.getDataFinal()));
    }

    @PostMapping
    public ResponseEntity<Conta> cadastrar(@Valid @RequestBody  Conta conta) {
        try {
            return ResponseEntity.ok(contaRepository.save(conta));
        } catch (Exception e) {
            logger.error("Erro ao consultar contas por descricao e data vencimento: "+e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<Conta> atualizar(@Valid @RequestBody  Conta conta) {
        try {
            if (conta.getId() != null) {
                Conta contaSalva = contaRepository.save(conta);
                return ResponseEntity.ok(contaSalva);
            }
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            logger.error("Erro ao consultar conta por daat de vencimento: "+e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/atualizar-situacao")
    public ResponseEntity<Conta> atualizarSituacao(@Valid @RequestBody ContaDTO contaDTO) {
        try {
            Optional<Conta> conta = contaRepository.findById(contaDTO.getId());

            if (conta.isPresent() && contaDTO.getSituacao() != null) {
                conta.get().setSituacao(SituacaoEnum.fromString(contaDTO.getSituacao()));
                return ResponseEntity.ok(contaRepository.save(conta.get()));
            }
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar situacao: "+e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/importar-csv")
    public ResponseEntity<List<Conta>> importarCsv(@RequestParam("file") MultipartFile file) {
        try {
            List<Conta> contas = csvImportService.importarCsv(file);
            contaRepository.saveAll(contas);

            return new ResponseEntity<>(contas, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}