package br.com.desafioitau.order.model;

import java.util.Collections;
import java.util.List;

public enum PolicySolicitation {

    RECEIVED() {
        @Override
        public List<PolicySolicitation> getNextSolicitations() {
            return List.of(PolicySolicitation.VALIDATED, PolicySolicitation.PENDING, PolicySolicitation.REJECTED, PolicySolicitation.CANCELED);
        }
    },
    VALIDATED() {
        @Override
        public List<PolicySolicitation> getNextSolicitations() {
            return List.of(PolicySolicitation.PENDING, PolicySolicitation.APPROVED, PolicySolicitation.REJECTED, PolicySolicitation.CANCELED);
        }
    },
    PENDING() {
        @Override
        public List<PolicySolicitation> getNextSolicitations() {
            return List.of(PolicySolicitation.APPROVED, PolicySolicitation.REJECTED, PolicySolicitation.CANCELED);
        }
    },
    REJECTED,
    APPROVED,
    CANCELED;

    public List<PolicySolicitation> getNextSolicitations() {
        return Collections.emptyList();
    }
}
