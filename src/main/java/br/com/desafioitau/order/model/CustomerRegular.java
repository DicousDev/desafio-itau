package br.com.desafioitau.order.model;

import java.math.BigDecimal;

public class CustomerRegular implements CustomerPolicyAnalyse {


    @Override
    public PolicySolicitation analyse(Policy policy) {

        PolicySolicitation result = PolicySolicitation.REJECTED;
        if(InsuranceCategory.LIFE.equals(policy.getCategory()) ||
           InsuranceCategory.RESIDENTIAL.equals(policy.getCategory())) {

            if(BigDecimal.valueOf(50000).compareTo(policy.getInsuredAmount()) >= 0) {
                result = PolicySolicitation.PENDING;
            }
        }
        else if(InsuranceCategory.AUTO.equals(policy.getCategory())) {

            if(BigDecimal.valueOf(350000).compareTo(policy.getInsuredAmount()) >= 0) {
                result = PolicySolicitation.PENDING;
            }
        }
        else if(BigDecimal.valueOf(255000).compareTo(policy.getInsuredAmount()) >= 0) {
            result = PolicySolicitation.PENDING;
        }

        return result;
    }
}
