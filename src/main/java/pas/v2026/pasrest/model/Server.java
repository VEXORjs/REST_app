package pas.v2026.pasrest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Server extends Resource{
    private String manufacturer;
    private String cpuName;
    private int numberOfCpus;
    private int ram;
    private int storageCapacity;
}
