/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vaccinationdistributionmodel.vaccination;

import vaccinationdistributionmodel.Modelable;
import vaccinationdistributionmodel.world.Globe;

/**
 *
 * @author ilari
 */
public interface VaccinationStrategy extends Modelable {
        
    public void createVaccinationSchedules(Globe globe);
    
}
