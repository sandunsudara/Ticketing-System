import { Routes } from '@angular/router';
import { HomeComponent } from './page/home/home.component';
import { ConfigComponent } from './page/config/config.component';
import { SimulationComponent } from './page/simulation/simulation.component';

export const routes: Routes = [
    {
        path:"",
        component : HomeComponent,
    },
    {
        path:"config",
        component:ConfigComponent
    },
    {
        path:"simulation",
        component:SimulationComponent
        
    }
];
