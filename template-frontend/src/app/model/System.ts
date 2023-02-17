export interface LocaleInfo {
  language: string;
  locale: string;
}

type Locale = "en" | "ro";

export const LOCALES: Record<Locale, LocaleInfo> = {
  en: {language: 'en', locale: 'en_US'},
  ro: {language: 'ro', locale: 'ro_RO'}
}
