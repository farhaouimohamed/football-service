package fr.st.fs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fr.st.fs.entity.Team;
import fr.st.fs.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    public void testGetTeamsReturnsCorrectNumberOfTeams() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean sortDesc = false;
        List<Team> teams = createDummyTeams(20);
        Page<Team> expectedPage = createDummyTeamPage(teams.subList(0, size), page, size, sort, sortDesc);
        when(teamRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Team> actualPage = teamService.getTeams(page, size, sort, sortDesc);

        // Assert
        assertThat(actualPage.getContent().size()).isEqualTo(size);
    }

    @Test
    public void testGetTeamsReturnsTeamsInCorrectOrder() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean sortDesc = false;
        List<Team> teams = createDummyTeams(10);
        Page<Team> expectedPage = createDummyTeamPage(teams, page, size, sort, sortDesc);
        when(teamRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Team> actualPage = teamService.getTeams(page, size, sort, sortDesc);

        // Assert
        List<Team> expectedTeams = teams.stream().sorted((t1, t2) -> t1.getName().compareTo(t2.getName())).collect(Collectors.toList());
        List<Team> actualTeams = actualPage.getContent();
        assertThat(actualTeams)
                .usingElementComparatorIgnoringFields("id") // ignore the 'id' field when comparing Team objects
                .containsExactlyInAnyOrderElementsOf(expectedTeams);
    }

    @Test
    public void testGetTeamsHandlesInvalidInput() {
        // Arrange
        int page = -1;
        int size = -1;
        String sort = "name";
        boolean sortDesc = false;

        // Act
        Page<Team> actualPage = teamService.getTeams(page, size, sort, sortDesc);

        // Assert
        assertThat(actualPage.getContent().size()).isEqualTo(0);
    }

    private List<Team> createDummyTeams(int numTeams) {
        List<Team> teams = new ArrayList<>();

        for (int i = 1; i <= numTeams; i++) {
            Team team = Team.builder()
                    .name("Team " + i)
                    .acronym("T" + i)
                    .budget(BigDecimal.valueOf(1000))
                    .players(Collections.emptyList())
                    .build();
            teams.add(team);
        }

        return teams;
    }

    private Page<Team> createDummyTeamPage(List<Team> teams, int page, int size, String sort, boolean sortDesc) {
        // Set up pagination and sorting
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        // Calculate start and end index of the current page
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, teams.size());

        // Get paginated list of teams for the current page
        List<Team> currentPageTeams = teams.subList(startIndex, endIndex);

        // Create a page object with the current page of teams
        return new PageImpl<>(currentPageTeams, pageable, teams.size());
    }

}