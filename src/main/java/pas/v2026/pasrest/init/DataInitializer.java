package pas.v2026.pasrest.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pas.v2026.pasrest.model.*;
import pas.v2026.pasrest.services.ResourceAllocationService;
import pas.v2026.pasrest.services.ResourceService;
import pas.v2026.pasrest.services.UserService;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    public final UserService userService;
    public final ResourceService resourceService;
    public final ResourceAllocationService allocationService;

    public DataInitializer(UserService userService,
                           ResourceService resourceService,
                           ResourceAllocationService allocationService) {
        this.userService = userService;
        this.resourceService = resourceService;
        this.allocationService = allocationService;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== INICJALIZACJA DANYCH TESTOWYCH ===");

        UserAdmin admin = UserAdmin.builder()
                .login("admin")
                .password("admin123")
                .active(true)
                .build();
        userService.saveUser(admin);

        ResourceManager manager = ResourceManager.builder()
                .login("manager")
                .password("man123")
                .active(true)
                .build();
        userService.saveUser(manager);

        Client client1 = Client.builder()
                .login("kient_aktywny")
                .password("pass123")
                .active(true)
                .build();
        userService.saveUser(client1);

        Client client2 = Client.builder()
                .login("klient_zablokowany")
                .password("pass123")
                .active(false)
                .build();
        userService.saveUser(client2);

        Server server = Server.builder()
                .name("Web-Server-01")
                .description("Główny serwer NGINX")
                .price(BigDecimal.valueOf(150.0))
                .manufacturer("Dell")
                .cpuName("Intel Xeon")
                .ram(64)
                .storageCapacity(2000)
                .build();
        resourceService.saveResource(server);

        DataCenter dataCenter = DataCenter.builder()
                .name("DC-Warszawa")
                .description("Centrum danych w stolicy")
                .price(BigDecimal.valueOf(5000.0))
                .location("Warszawa")
                .area(500.0)
                .powerCapacity(1000)
                .build();
        resourceService.saveResource(dataCenter);

        Cluster cluster = Cluster.builder()
                .name("Cluster-AI-Alpha")
                .description("Klaster obliczeniowy do zadań uczenia maszynowego")
                .price(BigDecimal.valueOf(12000.0))
                .numberOfServers(16)
                .architecture("x86_64")
                .build();
        resourceService.saveResource(cluster);

        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + (7L * 24 * 60 * 60 * 1000));

        allocationService.createAllocation(client1.getId(), server.getId(), startTime, endTime);

        System.out.println("=== INICJALIZACJA DANYCH TESTOWYCH ZAKOŃCZONA");
    }
}
