export interface Product {
  id?: string;
  name: string;
  description: string;
  price: number;
  category: string;
}

export enum ProductCategory {
  ELECTRONICS = 'ELECTRONICS',
  FASHION = 'FASHION',
  HOME_GARDEN = 'HOME_GARDEN',
  HEALTH_BEAUTY = 'HEALTH_BEAUTY',
  SPORTS_LEISURE = 'SPORTS_LEISURE',
  GROCERIES = 'GROCERIES',
  AUTOMOTIVE = 'AUTOMOTIVE',
  OTHER = 'OTHER'
}