package model;

import jakarta.persistence.*;

@Entity
@NamedStoredProcedureQuery(
        name = "associarCrachaLogica",
        procedureName = "associarCrachaLogica",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class)
        }
)
public class AssociarCrachaSP {
    @Id
    private int dummy;
}
