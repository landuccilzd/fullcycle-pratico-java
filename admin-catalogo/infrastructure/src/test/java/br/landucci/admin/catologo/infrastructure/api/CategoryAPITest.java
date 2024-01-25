package br.landucci.admin.catologo.infrastructure.api;

import br.landucci.admin.catologo.ControllerTest;
import br.landucci.admin.catologo.application.category.create.CreateCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.create.CreateCategoryUseCase;
import br.landucci.admin.catologo.infrastructure.category.models.CreateCategoryApiInputCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase useCase;

    @Test
    public void givenAValidCommand_whenCreateACategory_thenShouldReturnACreatedCategory() throws Exception {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;

        final var input =
                new CreateCategoryApiInputCommand(expectedName, expectedDescription, expectedActive);

        Mockito.when(useCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutputCommand.from("123")));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/123"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123")));

        Mockito.verify(useCase, Mockito.times(1)).execute(Mockito.argThat(command ->
                Objects.equals(expectedName, command.name())
                && Objects.equals(expectedDescription, command.description())
                && Objects.equals(expectedActive, command.active())
        ));
    }

}
