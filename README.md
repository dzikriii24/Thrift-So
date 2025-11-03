# Thrift So 🛍️

<div align="center">

![Thrift So](https://img.shields.io/badge/Thrift-So-orange?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=for-the-badge&logo=kotlin)
![Android](https://img.shields.io/badge/Android-8.1%2B-green?style=for-the-badge&logo=android)

**Aplikasi Belanja Thrift Bergaya Retro Dibangun dengan Kotlin**

[Fitur](#-fitur) • [Screenshots](#-screenshots) • [Instalasi](#-instalasi) • [Tech Stack](#-tech-stack) • [Arsitektur](#-arsitektur) • [Kontribusi](#-kontribusi)

</div>

## 📱 Tentang Thrift So

Thrift So adalah aplikasi e-commerce Android modern dengan estetika retro yang indah, dirancang untuk membeli dan menjual item fashion bekas. Dibangun dengan Kotlin dan praktik pengembangan Android modern, aplikasi ini menawarkan pengalaman belanja yang mulus dengan persistensi data lokal.

## 🎯 Screenshots

<div align="center">

### ✨ Tampilan Utama Aplikasi

| Splash Screen | Login | Registrasi |
|---------------|--------|------------|
| <img src="https://i.pinimg.com/736x/1f/66/2b/1f662bdd54345723bb68be3d95526470.jpg" width="200" alt="Splash Screen"> | <img src="https://i.pinimg.com/736x/ce/70/6b/ce706b6aafcb2679523669dc91be85c2.jpg" width="200" alt="Login"> | <img src="https://i.pinimg.com/736x/1d/95/a5/1d95a5e65ddd51ea806dfe2484e44950.jpg" width="200" alt="Registrasi"> |

**Splash Screen** - Tampilan pembuka dengan branding Thrift So yang menarik<br>
**Login** - Form login dengan desain retro yang elegan<br>
**Registrasi** - Form pendaftaran pengguna baru

</div>

<div align="center">

### 🏠 Beranda & Produk

| Home & Produk | Banner | Katalog Produk |
|---------------|---------|----------------|
| <img src="https://i.pinimg.com/736x/d2/06/56/d206569baf4912906f36965d37c70f5c.jpg" width="200" alt="Home & Produk"> | <img src="https://i.pinimg.com/736x/8c/bd/5c/8cbd5ceb3dfe924a95f00bd2384ac3a5.jpg" width="200" alt="Banner"> | <img src="https://i.pinimg.com/736x/60/7e/a2/607ea24f9cf526e509751c9802d3dfe2.jpg" width="200" alt="Produk"> |

**Home & Produk** - Tampilan beranda dengan produk unggulan<br>
**Banner** - Promo dan penawaran spesial dalam bentuk banner<br>
**Katalog Produk** - Daftar lengkap semua produk yang tersedia

</div>

<div align="center">

### 🛒 Fitur Belanja

| Keranjang | Detail Pesanan | Tambah Produk |
|-----------|----------------|---------------|
| <img src="https://i.pinimg.com/736x/39/ca/b9/39cab9ec5928ae3d898113dcc9a9fd66.jpg" width="200" alt="Keranjang"> | <img src="https://i.pinimg.com/736x/5f/d4/22/5fd4225d8fc78e01ce65788aca42f3f3.jpg" width="200" alt="Detail Pesanan"> | <img src="https://i.pinimg.com/736x/93/38/d3/9338d33c24446add93856654a919f761.jpg" width="200" alt="Tambah Produk"> |

**Keranjang** - Management item belanja dengan kuantitas<br>
**Detail Pesanan** - Ringkasan dan konfirmasi pesanan<br>
**Tambah Produk** - Form untuk menambah produk baru ke katalog

</div>

<div align="center">

### 👤 Profil & Konfirmasi

| Profil | Pesanan Berhasil |
|--------|------------------|
| <img src="https://i.pinimg.com/736x/9f/3e/b3/9f3eb3bdfe55bcaacc34312531b7409a.jpg" width="200" alt="Profil"> | <img src="https://i.pinimg.com/736x/cd/99/83/cd9983ffa1a2499a4d55100127faf194.jpg" width="200" alt="Pesanan Berhasil"> |

**Profil** - Informasi pengguna dan pengaturan akun<br>
**Pesanan Berhasil** - Konfirmasi pesanan berhasil diproses

</div>

## ✨ Fitur Utama

### 🛍️ Pengalaman Belanja Lengkap
- **Autentikasi Pengguna** - Login dan registrasi yang aman
- **Katalog Produk** - Browse produk dengan filter kategori
- **Keranjang Cerdas** - Management item dengan real-time update
- **Checkout Mudah** - Proses pembayaran yang simpel dan aman

### 🎨 Desain Retro Modern
- **UI/UX Menarik** - Kombinasi warna retro dengan layout modern
- **Navigasi Intuitif** - Bottom navigation yang mudah digunakan
- **Animasi Halus** - Transisi yang memperbaiki pengalaman pengguna

### 🔧 Teknologi Canggih
- **Local Database** - Penyimpanan data offline dengan JSON
- **Image Optimization** - Loading gambar yang cepat dan efisien
- **Form Validation** - Validasi input yang robust

## 🚀 Instalasi

### Prasyarat
- Android Studio Arctic Fox atau lebih baru
- Android SDK 27 (Android 8.1 Oreo) atau lebih tinggi
- Kotlin 1.9.0+

### Langkah Build
1. **Clone repository**
   ```bash
   git clone https://github.com/dzikriii24/thrift-so.git
   cd thrift-so
   ```

2. **Buka di Android Studio**
   - Buka Android Studio
   - Pilih "Open an existing project"
   - Navigasi ke direktori yang telah di-clone

3. **Build dan Run**
   - Hubungkan perangkat Android atau mulai emulator
   - Klik "Run" atau tekan `Shift + F10`

## 🛠 Tech Stack

### Teknologi Inti
- **Kotlin** - Bahasa pemrograman utama
- **Android SDK** - Pengembangan Android native
- **Material Design** - Komponen UI modern

### Arsitektur & Pola
- **MVVM Architecture** - Pola Model-View-ViewModel
- **Repository Pattern** - Lapisan abstraksi data
- **ViewBinding** - Referensi view yang type-safe

## 🏗 Struktur Aplikasi

```
app/
├── src/main/
│   ├── java/com/example/thriftso/
│   │   ├── activities/
│   │   │   └── MainActivity.kt
│   │   ├── fragments/
│   │   │   ├── HomeFragment.kt
│   │   │   ├── ProductsFragment.kt
│   │   │   ├── CartFragment.kt
│   │   │   ├── ProfileFragment.kt
│   │   │   └── CheckoutFragment.kt
│   │   ├── adapters/
│   │   │   ├── ProductAdapter.kt
│   │   │   └── CartAdapter.kt
│   │   ├── models/
│   │   │   ├── Product.kt
│   │   │   └── CartItem.kt
│   │   └── repo/
│   │       └── ThriftRepository.kt
```

## 🔧 Fitur Teknis

### 💾 Data Management
- **Local JSON Storage** - Tidak memerlukan database eksternal
- **CRUD Operations** - Create, Read, Update, Delete data produk
- **Cart Persistence** - Keranjang tersimpan secara lokal

### 🎨 UI Components
- **RecyclerView** - Menampilkan daftar produk yang efisien
- **CardView** - Tampilan produk yang menarik
- **BottomNavigation** - Navigasi antar halaman yang mudah

## 🤝 Kontribusi

Kami menyambut kontribusi! Silakan:
1. Fork repository ini
2. Buat branch fitur baru
3. Commit perubahan Anda
4. Push ke branch
5. Buat Pull Request

## 📄 Lisensi

Project ini dilisensikan di bawah Lisensi MIT - lihat file [LICENSE](LICENSE) untuk detail.

## 👨‍💻 Developer

**Dzikri**
- GitHub: [dzikriii24](https://github.com/dzikriii24)
- Email: dzikrirabbani2401@gmail.com

---

<div align="center">

**⭐ Jangan lupa beri bintang pada repository ini jika Anda merasa terbantu!**

Dibangun dengan ❤️ menggunakan Kotlin dan Android

</div>

## 📞 Dukungan

Jika Anda memiliki pertanyaan atau membutuhkan bantuan, silakan buka issue di GitHub repository.

---

**Thrift So** - Belanja thrift dengan gaya retro yang modern! 🛍️✨
