import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Product, ProductCategory } from "../features/products/models/product.model";
import { ProductService } from "../features/products/services/product.service";
import { AuthService } from '../auth/auth.service';
import { UserResponse } from '../auth/models/user.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  private productService = inject(ProductService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  expandedProductId: string | undefined;
  products: Product[] = [];
  userEmail: string = "";
  userRoles: string[] = [];
  categories = Object.values(ProductCategory);
  isEditModalOpen = false;
  productForm: FormGroup;
  currentEditingProductId: string | undefined;

  constructor() {
    this.productForm = this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      category: ['', [Validators.required]],
      price: [0, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit() {
    this.loadProducts();
    this.getUserInfo();
  }

  // User logic

  getUserInfo() {
    this.authService.whoami().subscribe({
      next: (response: UserResponse) => {
        this.userEmail = response.email;
        this.userRoles = response.roles;
      },
      error: () => this.logout()
    });
  }

  // Products logic
  
  loadProducts() {
    this.productService.getProducts().subscribe(data => this.products = data);
  }

  filterByCategory(category: string) {
    if (!category) {
      this.loadProducts();
      return;
    }
    this.productService.getProductsByCategory(category).subscribe(data => {
      this.products = data;
    });
  }


  editProduct(product: Product) {
    this.currentEditingProductId = product.id;
    this.isEditModalOpen = true;

    this.productForm.patchValue({
      name: product.name,
      description: product.description,
      category: product.category,
      price: product.price
    });
  }

  closeModal() {
    this.isEditModalOpen = false;
    this.currentEditingProductId = undefined;
    this.productForm.reset();
  }

  onSaveEdit() {
    if (this.productForm.invalid) return;

    const productData = this.productForm.value;

    if (this.currentEditingProductId) {
      // edit product
      this.productService.updateProduct(this.currentEditingProductId, productData).subscribe({
        next: (savedProduct) => {
          const index = this.products.findIndex(p => p.id === savedProduct.id);
          if (index !== -1) this.products[index] = savedProduct;
          this.closeModal();
        }
      });
    } else {
      // add new product
      this.productService.addProduct(productData).subscribe({
        next: (newProduct) => {
          this.products.push(newProduct); 
          this.closeModal();
        },
        error: (err) => console.error("Creation failed", err)
      });
    }
  }

  openCreateModal() {
    this.currentEditingProductId = undefined; 
    this.productForm.reset({ price: 0 });   
    this.isEditModalOpen = true;
  }

  deleteProduct(id: string | undefined) {
    if (!id) return;

    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.products = this.products.filter(product => product.id !== id);
          if (this.expandedProductId === id) {
            this.expandedProductId = undefined;
          }
        },
        error: () => console.error("Error deleting product with id: " + id)
      });
    }
  }

  //other stuff

  toggleDetails(productId: string | undefined) {
    this.expandedProductId = this.expandedProductId === productId ? undefined : productId;
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  get isAdmin(): boolean {
    return this.userRoles?.includes('ROLE_ADMIN');
  }
}
