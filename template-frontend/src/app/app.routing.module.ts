import {NgModule} from "@angular/core";
import {AuthGuard} from "./services/auth/auth.guard";
import {RegisterComponent} from "./modules/user/register/register.component";
import {LoginComponent} from "./modules/user/login/login.component";
import {HomePageComponent} from "./modules/home-page/home-page.component";
import {RouterModule, Routes} from "@angular/router";
import {EventsComponent} from "./modules/admin/events/events.component";
import {PropertiesComponent} from "./modules/admin/properties/properties.component";

const routes: Routes = [
  {path: '', component: HomePageComponent},

  {path: 'login', component: LoginComponent},
  {path: 'signup', component: RegisterComponent},

  {path: 'events', component: EventsComponent, canActivate: [AuthGuard]},
  {path: 'properties', component: PropertiesComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: '', pathMatch: 'full'}
];

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
