import org.springframework.test.jpa.AbstractJpaTests;

public class CreacionBBDD extends AbstractJpaTests{
    protected String[] getConfigLocations() {
        return new String[] { "classpath:/config/**/*.xml" };
    }    

    public CreacionBBDD(){}

	public void test(){  }
    
}
