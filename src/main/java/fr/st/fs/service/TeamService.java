package fr.st.fs.service;

import fr.st.fs.entity.Player;
import fr.st.fs.entity.Team;
import fr.st.fs.repository.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Returns a paginated list of teams sorted by the given field and direction.
     *
     * @param page     the page number (0-based)
     * @param size     the page size
     * @param sort     the sort field (name, acronym, budget)
     * @param sortDesc true for descending order, false for ascending order
     * @return a paginated list of teams
     */
    public Page<Team> getTeams(int page, int size, String sort, boolean sortDesc) {
        // Set up pagination and sorting
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        // Get paginated list of teams
        return teamRepository.findAll(pageable);
    }

    /**
     * Adds a new team with the given data.
     *
     * @param name    the name of the team
     * @param acronym the acronym of the team
     * @param budget  the budget of the team
     * @param players the list of players in the team (may be null or empty)
     * @return the newly created team
     */
    public Team addTeam(String name, String acronym, BigDecimal budget, List<Player> players) {
        // Create new team
        Team team = Team.builder().name(name).acronym(acronym).players(players).build();

        // Add players to team
        if (players != null) {
            players.forEach(player -> {
                player.setTeam(team);
                team.getPlayers().add(player);
            });
        }

        // Save team to database
        return teamRepository.save(team);
    }

}
