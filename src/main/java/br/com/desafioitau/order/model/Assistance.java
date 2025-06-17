package br.com.desafioitau.order.model;

import br.com.desafioitau.order.validator.GenericValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "assistance")
public class Assistance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String assistance;

    public static class AssistanceBuilder {

        public Assistance build() {

            GenericValidator.validateMaxLength(assistance, 255, "Assistance exceeded character limit");

            return new Assistance(id, assistance);
        }
    }
}
