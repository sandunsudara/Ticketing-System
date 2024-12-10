import { Routes } from '@angular/router';
import { HomeComponent } from './page/home/home.component';
import { ConfigComponent } from './page/config/config.component';
import { SimulationComponent } from './page/simulation/simulation.component';
import { LoggerComponent } from './page/logger/logger.component';

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
    },
    {
        path:"logger",
        component : LoggerComponent
    }
];
