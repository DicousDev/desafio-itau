package br.com.desafioitau.order.model;

import java.math.BigDecimal;

public class CustomerPreferential implements CustomerPolicyAnalyse {

    @Override
    public PolicySolicitation analyse(Policy policy) {
        PolicySolicitation result = PolicySolicitation.REJECTED;
        if(InsuranceCategory.LIFE.equals(policy.getCategory())) {

            if(BigDecimal.valueOf(800000).compareTo(policy.getInsuredAmount()) > 0) {
                result = PolicySolicitation.PENDING;
            }
        }
        else if(InsuranceCategory.AUTO.equals(policy.getCategory()) || InsuranceCategory.RESIDENTIAL.equals(policy.getCategory())) {

            if(BigDecimal.valueOf(45000).compareTo(policy.getInsuredAmount()) > 0) {
                result = PolicySolicitation.PENDING;
            }

        }
        else if(BigDecimal.valueOf(375000).compareTo(policy.getInsuredAmount()) >= 0) {
            result = PolicySolicitation.PENDING;
        }

        return result;
    }
}
