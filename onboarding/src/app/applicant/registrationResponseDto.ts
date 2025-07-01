export interface RegistrationResponseDto {
  applicationId: string;
  legalName: string;
  legalStructure: string;
  countryOfIncorporation: string;
  registrationNumber: string;
  taxId: string;
  industryType: string;
  directors: DirectorDto[];
  contactPerson: string;
  contactEmail: string;
  annualTurnover: string;
  monthlyVolume: string;
  ubos: UboDto[];
  statusMessage: string;
  fileInfo: FileInfo;
}

export interface FileInfo{
  fileName: string;
  fileType: string;
  fileSize: number;
  base64Content: string;
}

export interface DirectorDto {
  name: string;
  nationalId: string;
  countryOfResidence: string;
}

export interface UboDto {
  name: string;
  ownershipPercent: string;
  nationality: string;
}

export interface  ApplicationLeastResponseDto {
	applicationId: string;
	legalName: string;
	legalStructure: string;
	countryOfIncorporation: string;
	registrationNumber: string;
	industryType: string;
  status: string;
}