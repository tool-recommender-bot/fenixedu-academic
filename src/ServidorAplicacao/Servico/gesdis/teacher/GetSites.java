package ServidorAplicacao.Servico.gesdis.teacher;

/**
 * Servi�o ObterSitios.
 *
 * @author Joao Pereira
 * @author Ivo Brand�o
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class GetSites implements IServico {

  private static GetSites service = new GetSites();

  /**
   * The singleton access method of this class.
   */
  public static GetSites getService() {
    return service;
  }

  /**
   * The constructor of this class.
   */
  private GetSites() { }

  /**
   * Service name
   */
  public final String getNome() {
    return "GetSites";
  }

  /**
   * Executes the service. Returns the current collection of sites names.
   */
  public List run() throws FenixServiceException {
    ISite site = null;
    ISuportePersistente sp;
    List allSites = null;

	try {
    	sp = SuportePersistenteOJB.getInstance();
    	allSites = sp.getIPersistentSite().readAll();
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia.getMessage());
	}

    if (allSites == null || allSites.isEmpty()) throw new InvalidArgumentsServiceException();

    // build the result of this service
    Iterator iterator = allSites.iterator();
    List result = new ArrayList(allSites.size());
    
    while (iterator.hasNext())
		result.add( Cloner.copyISite2InfoSite((ISite) iterator.next()) );

    return result;
  }
}
