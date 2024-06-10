package com.kleben.contas.controller;

import com.kleben.contas.model.Conta;
import com.kleben.contas.security.JwtTokenProvider;
import com.kleben.contas.service.CsvImportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.kleben.contas.repository.ContaRepository;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = {ContaController.class, AutenticacaoController.class})
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private ContaRepository contaRepository;
    @MockBean
    private CsvImportService csvImportService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private String bearerToken;

    @BeforeEach
    public void setup() throws Exception {

        // Initialize MockMvc
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        // Simulate generating the token directly
        when(jwtTokenProvider.generateToken("user")).thenReturn("mocked-token");
        bearerToken = jwtTokenProvider.generateToken("user");

        when(jwtTokenProvider.validateJwtToken(bearerToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJwtToken(bearerToken)).thenReturn("user");
    }

    @Test
    public void getTarefa_ByExistingId_ReturnOk() throws Exception {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDescricao("Descrição 1");

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        mockMvc.perform(
                        get("/conta/buscar/1")
                                .header("Authorization", "Bearer " + bearerToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.descricao", is("Descrição 1")));
    }

    @Test
    public void listar_ReturnsPageOfContas() throws Exception {
        // Simulate a page of Conta entities
        Conta conta1 = new Conta();
        conta1.setId(1L);
        conta1.setDescricao("Descrição 1");

        Conta conta2 = new Conta();
        conta2.setId(2L);
        conta2.setDescricao("Descrição 2");

        List<Conta> contas = Arrays.asList(conta1, conta2);
        Page<Conta> contaPage = new PageImpl<>(contas);

        // Mock the repository response
        when(contaRepository.findAll(any(Pageable.class))).thenReturn(contaPage);

        // Perform the GET request and validate the response
        mockMvc.perform(get("/conta")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].descricao", is("Descrição 1")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].descricao", is("Descrição 2")));
    }
}
