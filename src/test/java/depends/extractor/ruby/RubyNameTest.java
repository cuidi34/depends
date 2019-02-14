package depends.extractor.ruby;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import depends.entity.ContainerEntity;
import depends.entity.FunctionEntity;
import depends.entity.PackageEntity;
import depends.entity.TypeEntity;
import depends.entity.repo.EntityRepo;
import depends.extractor.FileParser;

public class RubyNameTest extends RubyParserTest {
	@Before
	public void setUp() {
		super.init();
	}
	
	@Test
	public void test_parameter_should_be_created() throws IOException {
		String[] srcs = new String[] {
	    		"./src/test/resources/ruby-code-examples/ruby_name.rb",
	    	    };
	    
	    for (String src:srcs) {
		    FileParser parser = createFileParser(src);
		    parser.parse();
	    }
	    PackageEntity module = (PackageEntity)(entityRepo.getEntity("A.B.C"));
	    assertNotNull(module);
	}
	

}

