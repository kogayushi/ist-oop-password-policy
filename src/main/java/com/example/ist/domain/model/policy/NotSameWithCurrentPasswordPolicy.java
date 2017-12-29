package com.example.ist.domain.model.policy;

import com.example.ist.domain.model.identity.AuthenticationFactor;

public class NotSameWithCurrentPasswordPolicy implements Policy {
    /* private final Hahser hahser */
    private final String password;

    public NotSameWithCurrentPasswordPolicy(String password /* ,Hashser hahser */) {
        this.password = password;
    }

    @Override
    public boolean isSatisfiedBy(AuthenticationFactor factor) {
        // 本来は暗号化／ハッシュ化関数をコンストラクタでとり、暗号化／ハッシュ化した値と比較しなければならない
        return this.password.equals(/* hasher.hash( */ factor.getValue() /* ) */) == false;
    }
}
