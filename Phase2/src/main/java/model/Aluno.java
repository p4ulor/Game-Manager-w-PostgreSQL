/*
 * Copyright (c) 2023. Done by Paulo Rosa. All rights reserved to me.
 */

package model;

import jakarta.persistence.*;

@Entity
@Table(name="alunos")
@NamedQuery(name="Aluno.findAll", query="SELECT a FROM Aluno a")
public class Aluno {
    @Id
    private long numal;

    @Column(name="aluga_cac")
    private Integer alugaCac;

    private String nomeal;

    public Aluno() {
    }

    public long getNumal() {
        return this.numal;
    }

    public void setNumal(long numal) {
        this.numal = numal;
    }

    public Integer getAlugaCac() {
        return this.alugaCac;
    }

    public void setAlugaCac(Integer alugaCac) {
        this.alugaCac = alugaCac;
    }

    public String getNomeal() {
        return this.nomeal;
    }

    public void setNomeal(String nomeal) {
        this.nomeal = nomeal;
    }

}
