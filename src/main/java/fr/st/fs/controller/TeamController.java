package fr.st.fs.controller;

import fr.st.fs.entity.Player;
import fr.st.fs.entity.Team;
import fr.st.fs.payload.ApiResponse;
import fr.st.fs.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
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
    @GetMapping
    public ResponseEntity<?> getTeams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "false") boolean sortDesc) {
        try {
            return ResponseEntity.ok(teamService.getTeams(page, size, sort, sortDesc));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponse(false, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * Adds a new team with the given data.
     *
     * @param team    the team (we can use the DTO rather than the entity)
     * @return the newly created team
     */
    @PostMapping
    public ResponseEntity<?> addTeam(@RequestBody Team team) {
        try {
            return ResponseEntity.ok(teamService.addTeam(team.getName(), team.getAcronym(), team.getBudget(), team.getPlayers()));
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse(false, "Invalid input"+e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

