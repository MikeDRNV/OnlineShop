const getProducts = async () => {
  const response = await fetch('/api/v1/products');
  const body = await response.json();
  return body;
};

export default getProducts;
