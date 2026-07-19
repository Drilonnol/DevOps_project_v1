resource "aws_dynamodb_table" "cars" {
  name         = var.table_name
  hash_key     = var.hash_key
  billing_mode = var.billing_mode

  attribute {
    name = var.hash_key
    type = "S"
  }
}

resource "aws_dynamodb_table_item" "car_item" {
  table_name = aws_dynamodb_table.cars.name
  hash_key   = aws_dynamodb_table.cars.hash_key

  item = jsonencode({
    Manufacturer = {
      S = var.manufacturer
    }
    Make = {
      S = var.make
    }
    Year = {
      N = tostring(var.year)
    }
    VIN = {
      S = var.vin
    }
  })
}