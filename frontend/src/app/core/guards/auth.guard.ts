import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../auth/auth/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    // 1. If not logged in at all, go to login
    if (!authService.isLoggedIn()) {
        authService.logout(); // Clean up just in case
        router.navigate(['/login']);
        return false;
    }

    // 2. Get roles required for this specific route
    const expectedRoles = route.data['roles'] as Array<string>;

    // 3. If the route doesn't require specific roles, OR the user has the role, let them in
    if (!expectedRoles || authService.hasRole(expectedRoles)) {
        return true;
    }

    // 4. If they reach here, they are logged in but DON'T have the right role
    alert("Access Denied: You do not have permission for this page.");
    router.navigate(['/dashboard']); 
    return false;
};