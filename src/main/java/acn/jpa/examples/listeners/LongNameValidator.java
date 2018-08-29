package acn.jpa.examples.listeners;

import acn.jpa.examples.domains.NamedEntity;

import javax.persistence.PrePersist;

/**
 * Created by fabrice on 8/29/18.
 */
public class LongNameValidator {
    @PrePersist
    public void validateLongName(NamedEntity obj) {
        System.out.println("Listener "+ this.getClass().getName()+ "@PrePersist obj = [" + obj + "]");


    }
}
