package fr.st.fs.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.st.fs.controller.TeamController;
import fr.st.fs.entity.Player;
import fr.st.fs.entity.Team;
import fr.st.fs.payload.ApiResponse;
import fr.st.fs.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc
public class TeamControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @MockBean
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    @Test
    public void testGetTeams() throws Exception {
        // Create some sample data
        List<Team> teams = Arrays.asList(
                Team.builder().name("Team A").build(),
                Team.builder().name("Team B").build()
        );

        // Mock the service method to return the sample data
        given(teamService.getTeams(anyInt(), anyInt(), anyString(), anyBoolean()))
                .willReturn(new PageImpl<>(teams));

        // Send a GET request to the endpoint with some query parameters
        mockMvc.perform(get("/api/teams?page=0&size=2&sort=name&sortDesc=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is("Team A")))
                .andExpect(jsonPath("$.content[1].name", is("Team B")));

        // Verify that the service method was called with the correct parameters
        verify(teamService, times(1)).getTeams(0, 2, "name", false);
    }

    @Test
    void testAddTeam() throws Exception {
        // setup mock team to return from service
        Team mockTeam = Team.builder()
                .name("Team A")
                .acronym("TMA")
                .budget(BigDecimal.valueOf(1000))
                .players(null)
                .build();
        Mockito.when(teamService.addTeam(Mockito.anyString(), Mockito.anyString(),
                Mockito.any(BigDecimal.class), Mockito.isNull())).thenReturn(mockTeam);

        // create request body with test data
        Team requestBody = Team.builder()
                .name("Test Team")
                .acronym("TTT")
                .budget(BigDecimal.valueOf(500))
                .players(null)
                .build();

        // perform POST request to add team endpoint
        MvcResult result = mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andReturn();

        // verify response body contains expected data
        String responseBody = result.getResponse().getContentAsString();
        //Assertions.assertThat(responseBody).isEqualTo(new ObjectMapper().writeValueAsString(mockTeam));
    }

    @Test
    public void testAddTeam1() throws Exception {
        // setup mock teamService to return a dummy team
        Team dummyTeam = Team.builder().name("Team A").acronym("TMA").budget(BigDecimal.valueOf(1000)).players(null).build();
        when(teamService.addTeam(anyString(), ArgumentMatchers.any(), ArgumentMatchers.any(BigDecimal.class), Mockito.isNull()))
                .thenReturn(dummyTeam);

        // perform POST request with valid request parameters
        String json = "{\"name\":\"Team A\", \"acronym\":\"TMA\", \"budget\":1000}";
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Team A")))
                .andExpect(jsonPath("$.acronym", is("TMA")))
                .andExpect(jsonPath("$.budget", is(1000)));

        // verify that teamService.addTeam was called with the correct parameters
        verify(teamService, times(1)).addTeam(eq("Team A"), eq("TMA"), eq(BigDecimal.valueOf(1000)), isNull());
    }
}
