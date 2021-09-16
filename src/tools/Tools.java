package tools;

/**
 *
 * @author jdhein
 */
public class Tools {
    
    public static String converteCharArrayToString(char[] vetor){
        try{
            StringBuilder retorno = new StringBuilder();
            
            for(int i = 0; i < vetor.length; i++){
                retorno.append(vetor[i]);
            }
            
            return retorno.toString();
            
        }catch(Exception ex){
            return "";
        }
    }
    
}
