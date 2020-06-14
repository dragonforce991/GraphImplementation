package Testing;

import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import upo.greedy.Greedy;


/*
 * @Author Antonino Massarotti 20035564
 */
public class GreedyHuffmanTest {

	@Test
    public void HuffmanTest() {
    	Character[] alphabet = new Character[6];
    	int[] freq = new int[6];
    	char start = 'a';
    	for(int i=0; i<6; i++) {
    		alphabet[i] = start++;
    	}
    	
    	freq[0] = 45;
    	freq[1] = 13;
    	freq[2] = 12;
    	freq[3] = 16;
    	freq[4] = 9;
    	freq[5] = 5;
    	
    	Map<Character,String> result = Greedy.getHuffmanCodes(alphabet, freq);
    	Assert.assertEquals(result.get('a'), "0");
    	Assert.assertEquals(result.get('b'), "101");
    	Assert.assertEquals(result.get('c'), "100");
    	Assert.assertEquals(result.get('d'), "111");
    	Assert.assertEquals(result.get('e'), "1101");
    	Assert.assertEquals(result.get('f'), "1100");
    	
    }
}
