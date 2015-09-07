package org.minimalj.example.petclinic.model;

import java.util.List;
import java.util.Random;

import org.minimalj.backend.Backend;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.mock.MockName;
import org.minimalj.util.mock.Mocking;

public class Owner implements Mocking {
	public static final Owner $ = Keys.of(Owner.class);
	
	public Object id;
	
	public final Person person = new Person();
	
    @NotEmpty @Size(255)
    public String address;

    @NotEmpty @Size(80)
    public String city;

    @NotEmpty @Size(20)
    // @Digits(fraction = 0, integer = 10)
    public String telephone;
    
//    private transient List<Pet> pets;
    
    public List<Pet> getPets() {
    	if (Keys.isKeyObject(this)) return Keys.methodOf(this, "pets", List.class);
//    	if (pets == null) {
//    		pets = Backend.persistence().read(Pet.class, Criteria.equals(Pet.$.owner, this), 100);
//    	}
//		return pets;
    	return Backend.persistence().read(Pet.class, Criteria.equals(Pet.$.owner, this), 100);
	}
    
    @Override
    public void mock() {
    	person.mock();
    	address = MockName.street();
    	city = MockName.officialName() + "city";
    	telephone = String.valueOf(100000 + new Random().nextInt(900000));
    }

}