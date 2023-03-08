package fr.st.fs;

import fr.st.fs.entity.Player;
import fr.st.fs.service.TeamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FootballServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(TeamService teamService){
        return args -> {
            List<Player> playersTeam1 = new ArrayList<>();
            playersTeam1.add(Player.builder().name("John").position(4).build());
            playersTeam1.add(Player.builder().name("Adam").position(1).build());
            teamService.addTeam("Team 1", "T1", BigDecimal.valueOf(12.3), playersTeam1);

            List<Player> playersTeam2 = new ArrayList<>();
            playersTeam1.add(Player.builder().name("Sami").position(10).build());
            playersTeam1.add(Player.builder().name("Charles").position(7).build());
            teamService.addTeam("Team 2", "T2", BigDecimal.valueOf(14.3), playersTeam1);
        };
    }

}
