    package com.investimentos.CompraVendaAcoes.controller;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.investimentos.CompraVendaAcoes.dto.UsuarioDto;
    import com.investimentos.CompraVendaAcoes.exception.Error;
    import com.investimentos.CompraVendaAcoes.exception.GlobalExceptionHandler;
    import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioJaCadastradoException;
    import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioNaoEncontradoException;
    import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
    import com.investimentos.CompraVendaAcoes.service.UsuarioService;
    import com.investimentos.CompraVendaAcoes.service.util.UsuarioUtilService;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.test.web.servlet.MockMvc;

    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.Mockito.*;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
    import org.springframework.test.web.servlet.setup.MockMvcBuilders;

    import java.util.Collections;


    @ExtendWith(MockitoExtension.class)
    public class UsuarioControllerTest {

        @InjectMocks
        private UsuarioController usuarioController;

        @Mock
        private UsuarioService usuarioService;

        @Mock
        private UsuarioUtilService usuarioUtilService;

        private String userResponse;
        private UsuarioModel usuarioModel;
        private String jsonContent;
        private MockMvc mockMvc;
        private String arrayUsersResponse;
        private UsuarioModel usuarioModelAlterado;
        private UsuarioDto usuarioDtoAlterado;
        private ObjectMapper objectMapper;


        @BeforeEach
        void setUp() {
            mockMvc = MockMvcBuilders
                    .standaloneSetup(usuarioController)
                    .setControllerAdvice(new GlobalExceptionHandler())
                    .build();

            usuarioModel = new UsuarioModel();
            usuarioModel.setId(3L);
            usuarioModel.setCpf("82407211090");
            usuarioModel.setNome("Erinaldo Teste dos Santos");
            usuarioModel.setIdade(30);
            usuarioModel.setEmail("erinaldo@teste.com.br");

            userResponse = """
                    {
                        "id": 3,
                        "cpf": "82407211090",
                        "nome": "Erinaldo Teste dos Santos",
                        "idade": 30,
                        "email": "erinaldo@teste.com.br"
                    }
                    """;

            jsonContent = """
                    {
                        "cpf": "82407211090",
                        "nome": "Erinaldo Teste dos Santos",
                        "idade": 30,
                        "email": "erinaldo@teste.com.br"
                    }
                    """;

            arrayUsersResponse = """
                [
                    {
                        "id": 3,
                        "cpf": "82407211090",
                        "nome": "Erinaldo Teste dos Santos",
                        "idade": 30,
                        "email": "erinaldo@teste.com.br"
                    }
                ]
                """;
            usuarioDtoAlterado = new UsuarioDto(
                    "82407211090",
                    "Erinaldo Teste dos Santos - Alterado",
                    40,
                    "erinaldo@teste.com"
            );

            usuarioModelAlterado = new UsuarioModel();
            usuarioModelAlterado.setId(3L);
            usuarioModelAlterado.setCpf("82407211090");
            usuarioModelAlterado.setNome("Erinaldo Teste dos Santos - Alterado");
            usuarioModelAlterado.setIdade(40);
            usuarioModelAlterado.setEmail("erinaldo@teste.com");

            objectMapper = new ObjectMapper();
        }


        @Test
        @DisplayName("Deve cadastrar um usuário com sucesso e retornar o código 201")
        void deveCadastrarUsuarioComSucesso() throws Exception {
            when(usuarioService.cadastrarUsuario(any(UsuarioDto.class))).thenReturn(usuarioModel);

            mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(jsonContent))
                        .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/users/" + usuarioModel.getCpf()))
                    .andExpect(content().json(userResponse));

            verify(usuarioService, times(1)).cadastrarUsuario(any(UsuarioDto.class));
        }

        @Test
        @DisplayName("Deve lançar exceção para um usuário já cadastrado")
        void deveLancarExcecaoCadastrarUsuarioJaCadastrado() throws Exception {
           when(usuarioService.cadastrarUsuario(any(UsuarioDto.class)))
                    .thenThrow(new UsuarioJaCadastradoException("Usuário já cadastrado!!"));

           Error errorEsperado = new Error(HttpStatus.CONFLICT, "Usuário já cadastrado!!");

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(objectMapper.writeValueAsString(errorEsperado)));

            verify(usuarioService, times(1)).cadastrarUsuario(any(UsuarioDto.class));
        }

        @Test
        @DisplayName("Deve consultar a lista de usuário com sucesso e retornar o código ok")
        void deveConsultarListaDeUsuariosComSucesso() throws Exception {
            when(usuarioService.consultarUsuarios()).thenReturn(Collections.singletonList(usuarioModel));

            mockMvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(arrayUsersResponse));
            verify(usuarioService, times(1)).consultarUsuarios();
        }

        @Test
        @DisplayName("Deve consultar todos os usuários com a lista vazia")
        void deveConsultarUsuariosComListaVazia() throws Exception {
            when(usuarioService.consultarUsuarios()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));

            verify(usuarioService, times(1)).consultarUsuarios();
        }

        @Test
        @DisplayName("Deve alterar o usuário com sucesso")
        void deveAlterarUsuarioComSucesso() throws Exception {
            when(usuarioService.alterarUsuario("82407211090", usuarioDtoAlterado)).thenReturn(usuarioModelAlterado);

            mockMvc.perform(put("/users/82407211090")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usuarioDtoAlterado)))
                    .andExpect(status().isAccepted())
                    .andExpect(content().json(objectMapper.writeValueAsString(usuarioDtoAlterado)));

            verify(usuarioService, times(1)).alterarUsuario("82407211090", usuarioDtoAlterado);
        }

        @Test
        @DisplayName("Deve excluir um usuário utilizando o CPF")
        void deveExcluirUsuarioUsandoCPF() throws Exception {
            doNothing().when(usuarioService).excluirUsuarioByCpf("82407211090");

            mockMvc.perform(delete("/users/82407211090")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted())
                    .andExpect(content().string("Usuário 82407211090 excluído!"));

            verify(usuarioService, times(1)).excluirUsuarioByCpf("82407211090");
        }

        @Test
        @DisplayName(("Deve lançar exceção quando não localizar o usuário"))
        void deveLancarExcecaoQuandoNaoLocalizarUsuario() throws Exception {
            //configura o service para lançar exceçao
            doThrow(new UsuarioNaoEncontradoException("Usuário não localizado!"))
                    .when(usuarioService).excluirUsuarioByCpf("12345678900");

            //Cria o objeto do erro esperado
            Error errorEsperado = new Error(HttpStatus.NOT_FOUND, "Usuário não localizado!");

            mockMvc.perform(delete("/users/12345678900")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(objectMapper.writeValueAsString(errorEsperado)));

            verify(usuarioService, times(1)).excluirUsuarioByCpf("12345678900");
        }

        @Test
        @DisplayName("Deve excluir todos os usuários")
        void deveExcluirTodosUsuarios() throws Exception {
            doNothing().when(usuarioService).excluirTodosOsUsuarios();

            mockMvc.perform(delete("/users/delete")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted())
                    .andExpect(content().string("Os usuário foram excluídos!"));

            verify(usuarioService, times(1)).excluirTodosOsUsuarios();
        }
    }