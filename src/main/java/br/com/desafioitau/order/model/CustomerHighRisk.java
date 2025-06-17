package br.com.desafioitau.order.model;

import java.math.BigDecimal;

public class CustomerHighRisk implements CustomerPolicyAnalyse {

    @Override
    public PolicySolicitation analyse(Policy policy) {
        PolicySolicitation result = PolicySolicitation.REJECTED;

        if(InsuranceCategory.AUTO.equals(policy.getCategory())) {

            if(BigDecimal.valueOf(25000).compareTo(policy.getInsuredAmount()) >= 0) {
                result = PolicySolicitation.PENDING;
            }
        }
        else if(InsuranceCategory.RESIDENTIAL.equals(policy.getCategory())) {

            if(BigDecimal.valueOf(15000).compareTo(policy.getInsuredAmount()) >= 0) {
                result = PolicySolicitation.PENDING;
            }
        }
        else if(BigDecimal.valueOf(125000).compareTo(policy.getInsuredAmount()) >= 0) {
            result = PolicySolicitation.PENDING;
        }

        return result;
    }
}
