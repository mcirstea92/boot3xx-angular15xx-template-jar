package ro.mariuscirstea.template.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity(name = "cfg_configuration")
@Table(name = "cfg_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CFG_Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String category;

    private String name;

    private String value;

    /**
     * use values from java.sql.Types
     */
    private Integer type;

    private Integer version;

    private Timestamp created;

    private Long userId;

}
