package edu.ukma;

import edu.ukma.authentication.AuthenticationManager;
import edu.ukma.connection.ConnectionFactory;
import edu.ukma.domain.User;
import edu.ukma.repository.DatabaseInitializer;
import edu.ukma.repository.GoodRepository;
import edu.ukma.repository.UserRepository;
import edu.ukma.server.Server;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        DatabaseInitializer.createTables();
        UserRepository userRepository = new UserRepository(ConnectionFactory.getConnection());
        userRepository.save(User.builder()
                .username("user")
                .password(DigestUtils.md5Hex("password").toUpperCase())
                .build());

        AuthenticationManager authenticationManager = new AuthenticationManager(userRepository);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Server server = new Server(executorService, authenticationManager, new GoodRepository());
        server.run();
    }
}